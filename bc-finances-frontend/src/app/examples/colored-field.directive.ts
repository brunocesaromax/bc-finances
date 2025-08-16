import {Directive, HostBinding, HostListener, Input} from '@angular/core';

@Directive({
  // Seletor de atributo, poderia ser também apenas input
  selector: '[appColoredField]',
  exportAs: 'coloredField'
})
export class ColoredFieldDirective {

  @Input() color = 'gray';

  @HostBinding('style.backgroundColor') backgroundColor: string;

  constructor() {
  }

  @HostListener('focus') toColor() {
    // this.renderer.setStyle(this.elementRef.nativeElement,
    //   'backgroundColor', 'green');
    this.backgroundColor = this.color;
  }

  @HostListener('blur') discolor() {
    // this.renderer.setStyle(this.elementRef.nativeElement,
    //   'backgroundColor', 'transparent');
    this.backgroundColor = 'transparent';
  }
}
