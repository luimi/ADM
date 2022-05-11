import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AdmCoreModule, DeviceService, WebsocketService } from 'adm-core';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AdmCoreModule
  ],
  providers: [WebsocketService, DeviceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
