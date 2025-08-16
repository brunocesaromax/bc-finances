import { Component, Input, OnInit } from '@angular/core';
import { Contact } from '../../core/model';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-person-register-contact',
  templateUrl: './person-register-contact.component.html',
  styleUrls: ['./person-register-contact.component.css']
})
export class PersonRegisterContactComponent implements OnInit {

  @Input() contacts: Array<Contact>;
  showContactForm = false;
  contact: Contact;
  contactIndex: number;

  constructor() {
  }

  get isEdit() {
    return this.contact && this.contact.id;
  }

  ngOnInit() {
  }

  editContact(contact: Contact, index: number) {
    this.contact = this.cloneContact(contact);
    this.showContactForm = true;
    this.contactIndex = index;
  }

  deleteContact(index: number) {
    this.contacts.splice(index, 1);
  }

  buildNewContact() {
    this.contact = new Contact();
    this.contactIndex = this.contacts.length;
    this.showContactForm = true;
  }

  addContact(form: NgForm) {
    this.contacts[this.contactIndex] = this.cloneContact(this.contact);
    this.showContactForm = false;
    form.reset();
  }

  cloneContact(contact: Contact): Contact {
    return new Contact(contact.id, contact.name, contact.email, contact.phone);
  }
}
