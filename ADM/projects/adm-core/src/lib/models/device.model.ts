export class Device {
  id: string = "";
  ip: string = "";
  port: number = 0;
  manufacter: string = "";
  model: string = "";
  processor: string = "";
  version: string = "";
  versionSDK: string = "";
  diskFree: string = "";
  diskSpace: string = "";
  diskUsage: string = "";
  displayDPI: number = 0;
  installedApps: any[] = [];
  isADBWifi: boolean = false;
  isBluetooth: boolean = false;
  isRooted: boolean = false;
  screenTimeOut: number = 0;
  storedWifis: any[] = [];
  type: string = "device";
  public getUID(): string {
    return btoa(this.id + this.ip);
  }
}
