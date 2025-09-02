package br.com.bcfinances.person.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PersonRequest {

    private String name;
    private Boolean active;
    private AddressRequest address;
    private List<ContactRequest> contacts;

    @Setter
    @Getter
    @NoArgsConstructor
    public static class AddressRequest {
        private String street;
        private String number;
        private String complement;
        private String neighborhood;
        private String zipCode;
        private Long cityId;
        private CityRequest city;

        public Long getCityId() {
            return cityId != null ? cityId : (city != null ? city.getId() : null);
        }
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class CityRequest {
        private Long id;
        private String name;
        private StateRequest state;
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class StateRequest {
        private Long id;
        private String name;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    public static class ContactRequest {
        private Long id;
        private String name;
        private String email;
        private String phone;
    }
}