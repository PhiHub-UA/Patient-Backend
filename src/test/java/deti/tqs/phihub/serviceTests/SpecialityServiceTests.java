package deti.tqs.phihub.serviceTests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.services.SpecialityService;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class SpecialityServiceTests {

    @InjectMocks
    private SpecialityService specialityService;

    @Test
     void whenGetByID_thenSpecialityShouldBeReturned() {
        Speciality returned = specialityService.getSpecialityById(0);
        assertThat(returned).isEqualTo(Speciality.CARDIOLOGY);

        returned = specialityService.getSpecialityById(1);
        assertThat(returned).isEqualTo(Speciality.DERMATOLOGY);
    }

    @Test
     void whenGetAll_thenAllSpecialitiesShouldBeReturned() {
        List<String> returned = specialityService.getSpecialities();
        assertThat(returned.get(0)).isEqualTo("CARDIOLOGY");
        assertThat(returned.get(1)).isEqualTo("DERMATOLOGY");
    }
}
