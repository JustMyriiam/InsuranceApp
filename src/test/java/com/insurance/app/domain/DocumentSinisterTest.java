package com.insurance.app.domain;

import static com.insurance.app.domain.AccidentHistoryTestSamples.*;
import static com.insurance.app.domain.DocumentSinisterTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.insurance.app.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DocumentSinisterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DocumentSinister.class);
        DocumentSinister documentSinister1 = getDocumentSinisterSample1();
        DocumentSinister documentSinister2 = new DocumentSinister();
        assertThat(documentSinister1).isNotEqualTo(documentSinister2);

        documentSinister2.setId(documentSinister1.getId());
        assertThat(documentSinister1).isEqualTo(documentSinister2);

        documentSinister2 = getDocumentSinisterSample2();
        assertThat(documentSinister1).isNotEqualTo(documentSinister2);
    }

    @Test
    void accidentHistoryTest() {
        DocumentSinister documentSinister = getDocumentSinisterRandomSampleGenerator();
        AccidentHistory accidentHistoryBack = getAccidentHistoryRandomSampleGenerator();

        documentSinister.addAccidentHistory(accidentHistoryBack);
        assertThat(documentSinister.getAccidentHistories()).containsOnly(accidentHistoryBack);
        assertThat(accidentHistoryBack.getDocumentSinister()).isEqualTo(documentSinister);

        documentSinister.removeAccidentHistory(accidentHistoryBack);
        assertThat(documentSinister.getAccidentHistories()).doesNotContain(accidentHistoryBack);
        assertThat(accidentHistoryBack.getDocumentSinister()).isNull();

        documentSinister.accidentHistories(new HashSet<>(Set.of(accidentHistoryBack)));
        assertThat(documentSinister.getAccidentHistories()).containsOnly(accidentHistoryBack);
        assertThat(accidentHistoryBack.getDocumentSinister()).isEqualTo(documentSinister);

        documentSinister.setAccidentHistories(new HashSet<>());
        assertThat(documentSinister.getAccidentHistories()).doesNotContain(accidentHistoryBack);
        assertThat(accidentHistoryBack.getDocumentSinister()).isNull();
    }
}
