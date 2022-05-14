import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AdmCoreComponent } from './adm-core.component';
import { AppsComponent } from './components/apps/apps.component';
import { CheckComponent } from './components/check/check.component';
import { DevicesComponent } from './components/devices/devices.component';
import { DisplayComponent } from './components/display/display.component';
import { InformationComponent } from './components/information/information.component';
import { OperativeSystemComponent } from './components/os/os.component';
import { WifiComponent } from './components/wifi/wifi.component';

const components = [
  AdmCoreComponent,
    InformationComponent,
    OperativeSystemComponent,
    CheckComponent,
    DevicesComponent,
    DisplayComponent,
    WifiComponent,
    AppsComponent
];

@NgModule({
  declarations: components,
  imports: [
    BrowserModule,
    FormsModule
  ],
  exports: components
})
export class AdmCoreModule { }
