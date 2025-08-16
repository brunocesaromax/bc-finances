import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {TransactionsSearchComponent} from './transactions-search.component';

describe('TransactionsSearchComponent', () => {
  let component: TransactionsSearchComponent;
  let fixture: ComponentFixture<TransactionsSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TransactionsSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TransactionsSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
