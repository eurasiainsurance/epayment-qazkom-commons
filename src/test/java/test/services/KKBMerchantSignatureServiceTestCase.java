package test.services;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import javax.ejb.EJB;

import org.junit.Test;

import com.lapsa.kkb.services.KKBFormatException;
import com.lapsa.kkb.services.KKBMerchantSignatureService;
import com.lapsa.kkb.services.KKBServiceError;
import com.lapsa.kkb.services.KKBSignatureOperationFailed;
import com.lapsa.kkb.services.KKBWrongSignature;

public class KKBMerchantSignatureServiceTestCase extends ArquillianBaseTestCase {

    @EJB
    private KKBMerchantSignatureService merchantSignatureService;

    @Test
    public void testGetSignatureCertificate() {
	X509Certificate cert = merchantSignatureService.getSignatureCertificate();
	assertThat(cert, not(nullValue()));
	assertThat(cert.getSerialNumber(), is(new BigInteger("3246642955", 10)));
    }

    private static final byte[] SIGN_DATA = "sdgfhdjghfdskjghkjfehg8754th5g4ignu5".getBytes();

    private static final byte[] SIGN_DIGEST = new byte[] { -14, 24, 118, -102, -23, -19, 77, 12, 92, 56, -51,
	    74, -115, -23, 70, 120, -63, -118, 36, 38, -72, -92, -109, 95, -7, 97, 126, 75, -102, 67, -63, 21, -27, -11,
	    -27, -29, -45, -20, 125, -102, -9, 63, 116, -25, 111, -62, -14, -95, -118, -127, -65, 50, -34, -59, -125,
	    -107, -3, 124, -4, 122, 71, 120, -4, 17, -74, 28, 77, -53, 59, 72, -121, -38, 122, -120, 14, 1, 9, -68, 106,
	    -105, 35, 67, 62, 73, -119, 76, 109, 91, 87, 67, -60, -9, -93, -107, -64, 26, -14, -89, -117, -38, 68, 76,
	    17, 91, -29, -24, -93, 37, -43, -30, 72, 107, -11, 54, 101, 66, -20, 32, -37, 70, 54, -79, -21, 23, -99,
	    116, -74, 75 };

    @Test
    public void testSign() throws KKBSignatureOperationFailed {
	byte[] signature = merchantSignatureService.sign(SIGN_DATA);
	assertThat("Wrong signature", signature, is(SIGN_DIGEST));
    }

    private static final byte[] VERIFY_SUCCESS_SIGNATURE_DATA = "sdgfhdjghfdskjghkjfehg8754th5g4ignu5".getBytes();
    private static final byte[] VERIFY_SUCCESS_DIGEST = new byte[] { -14, 24, 118, -102, -23, -19, 77, 12, 92, 56, -51,
	    74, -115, -23, 70, 120, -63, -118, 36, 38, -72, -92, -109, 95, -7, 97, 126, 75, -102, 67, -63, 21, -27, -11,
	    -27, -29, -45, -20, 125, -102, -9, 63, 116, -25, 111, -62, -14, -95, -118, -127, -65, 50, -34, -59, -125,
	    -107, -3, 124, -4, 122, 71, 120, -4, 17, -74, 28, 77, -53, 59, 72, -121, -38, 122, -120, 14, 1, 9, -68, 106,
	    -105, 35, 67, 62, 73, -119, 76, 109, 91, 87, 67, -60, -9, -93, -107, -64, 26, -14, -89, -117, -38, 68, 76,
	    17, 91, -29, -24, -93, 37, -43, -30, 72, 107, -11, 54, 101, 66, -20, 32, -37, 70, 54, -79, -21, 23, -99,
	    116, -74, 75 };

    @Test
    public void testVerify_Success() throws KKBServiceError, KKBWrongSignature, KKBFormatException {
	merchantSignatureService.verify(VERIFY_SUCCESS_SIGNATURE_DATA, VERIFY_SUCCESS_DIGEST);
    }

    private static final byte[] VERIFY_FAIL_INVALID_DATA = "sdgfhdjghfdskjghkjfehg8754th5g4ignu5".getBytes();
    private static final byte[] VERIFY_FAIL_INVALID_DIGEST = new byte[] { -14, 118, 54, -102, -23, -19, 77, 12, 92, 56,
	    -51, 74, -115, -23, 70, 120, -63, -118, 36, 38, -72, -92, -109, 95, -7, 97, 126, 75, -102, 67, -63, 21, -27,
	    -11, -27, -29, -45, -20, 125, -102, -9, 63, 116, -25, 111, -62, -14, -95, -118, -127, -65, 50, -34, -59,
	    -125, -107, -3, 124, -4, 122, 71, 120, -4, 17, -74, 28, 77, -53, 59, 72, -121, -38, 122, -120, 14, 1, 9,
	    -68, 106, -105, 35, 67, 62, 73, -119, 76, 109, 91, 87, 67, -60, -9, -93, -107, -64, 26, -14, -89, -117, -38,
	    68, 76, 17, 91, -29, -24, -93, 37, -43, -30, 72, 107, -11, 54, 101, 66, -20, 32, -37, 70, 54, -79, -21, 23,
	    -99, 116, -74, 75 };

    @Test(expected = KKBWrongSignature.class)
    public void testVerify_Fail_Invalid() throws KKBServiceError, KKBWrongSignature, KKBFormatException {
	merchantSignatureService.verify(VERIFY_FAIL_INVALID_DATA, VERIFY_FAIL_INVALID_DIGEST);
    }

    private static final byte[] VERIFY_FAIL_BROKEN_DATA = "sdgfhdjghfdskjghkjfehg8754th5g4ignu5".getBytes();
    private static final byte[] VERIFY_FAIL_BROKEN_DIGEST = new byte[] { -14, 118, -102, -23, -19, 77, 12, 92, 56, -51,
	    74, -115, -23, 70, 120, -63, -118, 36, 38, -72, -92, -109, 95, -7, 97, 126, 75, -102, 67, -63, 21, -27, -11,
	    -27, -29, -45, -20, 125, -102, -9, 63, 116, -25, 111, -62, -14, -95, -118, -127, -65, 50, -34, -59, -125,
	    -107, -3, 124, -4, 122, 71, 120, -4, 17, -74, 28, 77, -53, 59, 72, -121, -38, 122, -120, 14, 1, 9, -68, 106,
	    -105, 35, 67, 62, 73, -119, 76, 109, 91, 87, 67, -60, -9, -93, -107, -64, 26, -14, -89, -117, -38, 68, 76,
	    17, 91, -29, -24, -93, 37, -43, -30, 72, 107, -11, 54, 101, 66, -20, 32, -37, 70, 54, -79, -21, 23, -99,
	    116, -74, 75 };

    @Test(expected = KKBFormatException.class)
    public void testVerify_Fail_Broken() throws KKBServiceError, KKBWrongSignature, KKBFormatException {
	merchantSignatureService.verify(VERIFY_FAIL_BROKEN_DATA, VERIFY_FAIL_BROKEN_DIGEST);
    }
}