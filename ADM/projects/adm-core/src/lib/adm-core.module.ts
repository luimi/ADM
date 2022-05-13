import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AdmCoreComponent } from './adm-core.component';
import { CheckComponent } from './components/check/check.component';
import { DevicesComponent } from './components/devices/devices.component';
import { DisplayComponent } from './components/display/display.component';
import { InformationComponent } from './components/information/information.component';
import { OperativeSystemComponent } from './components/os/os.component';



@NgModule({
  declarations: [
    AdmCoreComponent,
    InformationComponent,
    OperativeSystemComponent,
    CheckComponent,
    DevicesComponent,
    DisplayComponent
  ],
  imports: [
    BrowserModule,
    FormsModule
  ],
  exports: [
    AdmCoreComponent,
    InformationComponent,
    OperativeSystemComponent,
    CheckComponent,
    DevicesComponent,
    DisplayComponent
  ]
})
export class AdmCoreModule { }
