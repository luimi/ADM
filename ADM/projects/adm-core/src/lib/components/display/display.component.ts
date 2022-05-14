import { Component, OnInit } from '@angular/core';
import { DPIs, TimeOuts } from '../../utils/constants';
import { DeviceComponent } from '../device/device.component';

@Component({
  selector: 'device-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.scss']
})
export class DisplayComponent extends DeviceComponent implements OnInit {
  DPIs = DPIs;
  TimeOuts = TimeOuts;
  constructor() { 
      super()
  }

  ngOnInit(): void {
  }
  setDPI(){
    this.send({type:'setdpi', value1:this.device.displayDPI});
  }
  resetDPI(){
    this.send({type:'resetdpi'});
  }
  setTimeout(){
    this.send({type:'setscreentimeout', value1:this.device.screenTimeOut})
  }
}
