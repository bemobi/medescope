package br.com.bemobi.medescope;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import br.com.bemobi.medescope.repository.DownloadDataRepository;
import br.com.bemobi.medescope.repository.impl.MapDownloadDataRepository;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by bkosawa on 22/05/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class DownloadDataRepoTest {

    @Mock
    private DownloadDataRepository repository;

    @Before
    public void setup(){
        //initMocks(this);
        repository = MapDownloadDataRepository.getInstance(Robolectric.application.getApplicationContext());
    }

    @Test
    public void shouldRufflingANumberBetweenTwoNumbers() {
        String key = "key1";
        String value = "value1";

        repository.putDownloadData(key, value);

//        repository = null;
//        repository = MapDownloadDataRepository.getInstance(Robolectric.application.getApplicationContext());

        assertTrue(value.equals(repository.getDownloadData(key)));
    }
}
