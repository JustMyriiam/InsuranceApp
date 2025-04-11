import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { DocumentSinisterService } from '../service/document-sinister.service';
import { IDocumentSinister } from '../document-sinister.model';
import { DocumentSinisterFormService } from './document-sinister-form.service';

import { DocumentSinisterUpdateComponent } from './document-sinister-update.component';

describe('DocumentSinister Management Update Component', () => {
  let comp: DocumentSinisterUpdateComponent;
  let fixture: ComponentFixture<DocumentSinisterUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let documentSinisterFormService: DocumentSinisterFormService;
  let documentSinisterService: DocumentSinisterService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [DocumentSinisterUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DocumentSinisterUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DocumentSinisterUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    documentSinisterFormService = TestBed.inject(DocumentSinisterFormService);
    documentSinisterService = TestBed.inject(DocumentSinisterService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const documentSinister: IDocumentSinister = { id: 9180 };

      activatedRoute.data = of({ documentSinister });
      comp.ngOnInit();

      expect(comp.documentSinister).toEqual(documentSinister);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentSinister>>();
      const documentSinister = { id: 25073 };
      jest.spyOn(documentSinisterFormService, 'getDocumentSinister').mockReturnValue(documentSinister);
      jest.spyOn(documentSinisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentSinister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentSinister }));
      saveSubject.complete();

      // THEN
      expect(documentSinisterFormService.getDocumentSinister).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(documentSinisterService.update).toHaveBeenCalledWith(expect.objectContaining(documentSinister));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentSinister>>();
      const documentSinister = { id: 25073 };
      jest.spyOn(documentSinisterFormService, 'getDocumentSinister').mockReturnValue({ id: null });
      jest.spyOn(documentSinisterService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentSinister: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: documentSinister }));
      saveSubject.complete();

      // THEN
      expect(documentSinisterFormService.getDocumentSinister).toHaveBeenCalled();
      expect(documentSinisterService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDocumentSinister>>();
      const documentSinister = { id: 25073 };
      jest.spyOn(documentSinisterService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ documentSinister });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(documentSinisterService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
