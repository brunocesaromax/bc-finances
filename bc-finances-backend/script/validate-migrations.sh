#!/bin/sh

set -e

echo "Validating Flyway migrations..."

MIGRATIONS_DIR="./src/main/resources/db/migration"

if [ ! -d "$MIGRATIONS_DIR" ]; then
    echo "Migrations directory not found: $MIGRATIONS_DIR"
    exit 1
fi

TOTAL_FILES=0
VALID_FILES=0
ERROR_COUNT=0

echo "Checking files in: $MIGRATIONS_DIR"

echo ""

validate_filename() {
    file="$1"
    basename=$(basename "$file")

    printf '%s' "$basename" | grep -Eq '^V[0-9]{12}__[a-zA-Z0-9_-]+\.sql$'
}

validate_location() {
    file="$1"
    basename=$(basename "$file")

    year_month=$(printf '%s' "$basename" | sed -n 's/^V\([0-9]\{4\}\)\([0-9]\{2\}\)[0-9]\{6\}__.*\.sql$/\1 \2/p')
    if [ -n "$year_month" ]; then
        year=${year_month%% *}
        month=${year_month#* }
        expected_path="$MIGRATIONS_DIR/$year/$month"
        actual_dir=$(dirname "$file")

        if [ "$actual_dir" = "$expected_path" ]; then
            return 0
        fi

        echo "$basename"
        echo "  Wrong directory:"
        echo "  Expected: $expected_path"
        echo "  Actual:   $actual_dir"
        echo ""
        return 1
    fi

    return 1
}

tmp_file=$(mktemp)
find "$MIGRATIONS_DIR" -name "*.sql" -type f | sort > "$tmp_file"

while IFS= read -r file; do
    TOTAL_FILES=$((TOTAL_FILES + 1))
    basename=$(basename "$file")

    file_valid=1

    if ! validate_filename "$file"; then
        echo "$basename"
        echo "  Invalid name (expected: V{YYYYMMDDHHMM}__{description}.sql)"
        echo ""
        ERROR_COUNT=$((ERROR_COUNT + 1))
        file_valid=0
    fi

    if ! validate_location "$file"; then
        ERROR_COUNT=$((ERROR_COUNT + 1))
        file_valid=0
    fi

    if [ "$file_valid" -eq 1 ]; then
        VALID_FILES=$((VALID_FILES + 1))
    fi

done < "$tmp_file"

rm -f "$tmp_file"

echo "Checking for duplicate timestamps..."
echo ""

duplicates=$(find "$MIGRATIONS_DIR" -name "V*.sql" -type f -exec basename {} \; \
    | sed 's/^V\([0-9]\{12\}\)__.*/\1/' \
    | sort | uniq -d)

if [ -n "$duplicates" ]; then
    for timestamp in $duplicates; do
        ERROR_COUNT=$((ERROR_COUNT + 1))
        echo "Duplicate timestamp: $timestamp"
        echo "  Files:"
        find "$MIGRATIONS_DIR" -name "V${timestamp}__*.sql" -type f -exec echo "  - {}" \;
        echo ""
    done
fi

echo ""
echo "Validation summary:"
echo "  Total files: $TOTAL_FILES"
echo "  Valid files: $VALID_FILES"
echo "  Errors found: $ERROR_COUNT"

if [ "$ERROR_COUNT" -eq 0 ]; then
    echo "All migrations are valid."
    exit 0
fi

echo "Validation failed with $ERROR_COUNT error(s)"
exit 1
