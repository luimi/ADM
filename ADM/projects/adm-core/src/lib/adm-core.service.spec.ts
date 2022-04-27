import { TestBed } from '@angular/core/testing';

import { AdmCoreService } from './adm-core.service';

describe('AdmCoreService', () => {
  let service: AdmCoreService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdmCoreService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
