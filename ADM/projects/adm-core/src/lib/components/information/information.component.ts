import { Component, Input, OnInit } from '@angular/core';
import { Device } from '../../models/device.model';

@Component({
    selector: 'device-information',
    templateUrl: './information.component.html',
    styleUrls: ['./information.component.scss']
})
export class InformationComponent implements OnInit {
    @Input() device: Device = new Device()
    information: any;
    constructor() { }

    ngOnInit(): void {
        let info = [];
        info.push({title:'IP',description:this.device.ip});
        info.push({title:'Manufacter',description:this.device.manufacter});
        info.push({title:'Model',description:this.device.model});
        info.push({title:'Version',description:this.device.version});
        info.push({title:'SDK',description:this.device.versionSDK});
        info.push({title:'Processor',description:this.device.processor});
        this.information = info;
    }

}