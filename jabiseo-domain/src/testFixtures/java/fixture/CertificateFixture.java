package fixture;

import com.jabiseo.certificate.domain.Certificate;

public class CertificateFixture {

    public static Certificate createCertificate(Long certificateId) {
        return Certificate.of(certificateId, "certificate name");
    }

}
