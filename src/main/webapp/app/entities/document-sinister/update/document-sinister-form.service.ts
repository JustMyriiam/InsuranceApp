import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentSinister, NewDocumentSinister } from '../document-sinister.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentSinister for edit and NewDocumentSinisterFormGroupInput for create.
 */
type DocumentSinisterFormGroupInput = IDocumentSinister | PartialWithRequiredKeyOf<NewDocumentSinister>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentSinister | NewDocumentSinister> = Omit<T, 'issueDate' | 'expiryDate'> & {
  issueDate?: string | null;
  expiryDate?: string | null;
};

type DocumentSinisterFormRawValue = FormValueOf<IDocumentSinister>;

type NewDocumentSinisterFormRawValue = FormValueOf<NewDocumentSinister>;

type DocumentSinisterFormDefaults = Pick<NewDocumentSinister, 'id' | 'issueDate' | 'expiryDate'>;

type DocumentSinisterFormGroupContent = {
  id: FormControl<DocumentSinisterFormRawValue['id'] | NewDocumentSinister['id']>;
  documentId: FormControl<DocumentSinisterFormRawValue['documentId']>;
  documentName: FormControl<DocumentSinisterFormRawValue['documentName']>;
  issueDate: FormControl<DocumentSinisterFormRawValue['issueDate']>;
  expiryDate: FormControl<DocumentSinisterFormRawValue['expiryDate']>;
  associatedSinister: FormControl<DocumentSinisterFormRawValue['associatedSinister']>;
};

export type DocumentSinisterFormGroup = FormGroup<DocumentSinisterFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentSinisterFormService {
  createDocumentSinisterFormGroup(documentSinister: DocumentSinisterFormGroupInput = { id: null }): DocumentSinisterFormGroup {
    const documentSinisterRawValue = this.convertDocumentSinisterToDocumentSinisterRawValue({
      ...this.getFormDefaults(),
      ...documentSinister,
    });
    return new FormGroup<DocumentSinisterFormGroupContent>({
      id: new FormControl(
        { value: documentSinisterRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      documentId: new FormControl(documentSinisterRawValue.documentId),
      documentName: new FormControl(documentSinisterRawValue.documentName),
      issueDate: new FormControl(documentSinisterRawValue.issueDate),
      expiryDate: new FormControl(documentSinisterRawValue.expiryDate),
      associatedSinister: new FormControl(documentSinisterRawValue.associatedSinister),
    });
  }

  getDocumentSinister(form: DocumentSinisterFormGroup): IDocumentSinister | NewDocumentSinister {
    return this.convertDocumentSinisterRawValueToDocumentSinister(
      form.getRawValue() as DocumentSinisterFormRawValue | NewDocumentSinisterFormRawValue,
    );
  }

  resetForm(form: DocumentSinisterFormGroup, documentSinister: DocumentSinisterFormGroupInput): void {
    const documentSinisterRawValue = this.convertDocumentSinisterToDocumentSinisterRawValue({
      ...this.getFormDefaults(),
      ...documentSinister,
    });
    form.reset(
      {
        ...documentSinisterRawValue,
        id: { value: documentSinisterRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentSinisterFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      issueDate: currentTime,
      expiryDate: currentTime,
    };
  }

  private convertDocumentSinisterRawValueToDocumentSinister(
    rawDocumentSinister: DocumentSinisterFormRawValue | NewDocumentSinisterFormRawValue,
  ): IDocumentSinister | NewDocumentSinister {
    return {
      ...rawDocumentSinister,
      issueDate: dayjs(rawDocumentSinister.issueDate, DATE_TIME_FORMAT),
      expiryDate: dayjs(rawDocumentSinister.expiryDate, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentSinisterToDocumentSinisterRawValue(
    documentSinister: IDocumentSinister | (Partial<NewDocumentSinister> & DocumentSinisterFormDefaults),
  ): DocumentSinisterFormRawValue | PartialWithRequiredKeyOf<NewDocumentSinisterFormRawValue> {
    return {
      ...documentSinister,
      issueDate: documentSinister.issueDate ? documentSinister.issueDate.format(DATE_TIME_FORMAT) : undefined,
      expiryDate: documentSinister.expiryDate ? documentSinister.expiryDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
