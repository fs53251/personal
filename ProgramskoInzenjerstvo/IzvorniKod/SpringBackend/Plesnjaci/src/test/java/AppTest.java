//DEPLOY NE RADI SA OVIME
////////////////////////

//import static org.junit.Assert.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import javax.transaction.Transactional;
//
//import org.aspectj.lang.annotation.Before;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.BDDMockito.willDoNothing;
//import static org.mockito.Mockito.*;
//
//import java.util.List;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.google.common.base.Optional;
//import progi.projekt.backend.model.Klijent;
//import progi.projekt.backend.repository.KlijentRepository;
//import progi.projekt.backend.serviceImpl.KlijentImpl;
//@RunWith(MockitoJUnitRunner.Silent.class)
//public class AppTest 
//{ 
//
//	@Mock
//    private KlijentRepository repository; 
//	
//	@InjectMocks
//    private KlijentImpl klijentImpl;
//	
//    private Klijent klijent;
//    @BeforeEach
//    public void initMocks() {
//        MockitoAnnotations.initMocks(this);
//    }
//    @Test
//    public void testGetUserFound()
//    {
//    	String username = "user1";
//    	klijent = new Klijent();
//    	klijent.setKorisnicko_ime(username);
//    	klijent.setIme("Luka");
//    	when(repository.dohvatKlijenta(username)).thenReturn(klijent);	
//         Klijent kl = klijentImpl.dohvatiKlijenta(username);
//    	assertEquals("Luka",kl.getIme());
//    }
//    @Test
//    public void testGetUserNotFound()
//    {
//    	String username = "user1";
//    	klijent = new Klijent();
//    	klijent.setKorisnicko_ime(username);
//    	klijent.setIme("Luka");
//    	when(repository.dohvatKlijenta(username)).thenReturn(klijent);	
//         Klijent kl = klijentImpl.dohvatiKlijenta("fake_user");
//    	assertEquals(null,kl);
//    }
//    @Test
//    public void testDeleteUserNotFound() {
//    	String username = "user1";
//    	klijent = new Klijent();
//    	klijent.setKorisnicko_ime(username);
//    	klijent.setIme("Luka");
//    	when(repository.dohvatKlijenta(username)).thenReturn(klijent);	
//         Klijent kl = klijentImpl.obrisiKlijenta("fake_user");
//    	assertEquals(null,kl);
//    	
//    }
//}
