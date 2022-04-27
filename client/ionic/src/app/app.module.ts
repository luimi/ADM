import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouteReuseStrategy } from '@angular/router';

import { IonicModule, IonicRouteStrategy } from '@ionic/angular';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AdmCoreModule, DeviceService, WebsocketService } from 'adm-core';

@NgModule({
  declarations: [AppComponent],
  entryComponents: [],
  imports: [BrowserModule, IonicModule.forRoot(), AppRoutingModule, AdmCoreModule],
  providers: [{ provide: RouteReuseStrategy, useClass: IonicRouteStrategy },WebsocketService, DeviceService],
  bootstrap: [AppComponent],
})
export class AppModule {}
