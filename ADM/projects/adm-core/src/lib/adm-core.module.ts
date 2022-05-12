import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AdmCoreComponent } from './adm-core.component';
import { InformationComponent } from './components/information/information.component';



@NgModule({
  declarations: [
    AdmCoreComponent,
    InformationComponent
  ],
  imports: [
    BrowserModule
  ],
  exports: [
    AdmCoreComponent,
    InformationComponent
  ]
})
export class AdmCoreModule { }
