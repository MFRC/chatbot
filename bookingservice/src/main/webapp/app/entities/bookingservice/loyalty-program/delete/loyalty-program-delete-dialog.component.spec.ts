jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { LoyaltyProgramService } from '../service/loyalty-program.service';

import { LoyaltyProgramDeleteDialogComponent } from './loyalty-program-delete-dialog.component';

describe('LoyaltyProgram Management Delete Component', () => {
  let comp: LoyaltyProgramDeleteDialogComponent;
  let fixture: ComponentFixture<LoyaltyProgramDeleteDialogComponent>;
  let service: LoyaltyProgramService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [LoyaltyProgramDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(LoyaltyProgramDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(LoyaltyProgramDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(LoyaltyProgramService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete('9fec3727-3421-4967-b213-ba36557ca194');
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith('9fec3727-3421-4967-b213-ba36557ca194');
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
