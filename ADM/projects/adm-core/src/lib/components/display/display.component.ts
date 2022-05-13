import { Component, OnInit } from '@angular/core';
import { DPIs } from '../../utils/constants';
import { DeviceComponent } from '../device/device.component';

@Component({
  selector: 'device-display',
  templateUrl: './display.component.html',
  styleUrls: ['./display.component.scss']
})
export class DisplayComponent extends DeviceComponent implements OnInit {
  DPIs = DPIs
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
}
