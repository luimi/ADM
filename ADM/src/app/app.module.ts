import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { AdmCoreModule, DeviceService, WebsocketService } from 'projects/adm-core/src/public-api';

import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AdmCoreModule,
    FormsModule
  ],
  providers: [WebsocketService, DeviceService],
  bootstrap: [AppComponent]
})
export class AppModule { }
