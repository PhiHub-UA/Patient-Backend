package deti.tqs.phihub.controllerTests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import deti.tqs.phihub.configs.SecurityFilter;
import deti.tqs.phihub.configs.TokenProvider;
import deti.tqs.phihub.controllers.AppointmentController;
import deti.tqs.phihub.models.Appointment;
import deti.tqs.phihub.models.Speciality;
import deti.tqs.phihub.models.User;
import deti.tqs.phihub.repositories.UserRepository;
import deti.tqs.phihub.services.AppointmentService;
import deti.tqs.phihub.services.AuthService;
import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.UserService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)

class AppointmentControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AppointmentService service;
    @MockBean
    private UserService userService;
    @MockBean
    private SpecialityService specialityService;

    @MockBean
    private TokenProvider tokenProvider;
    @MockBean
    private SecurityFilter securityFilter;

    private Appointment app0 = new Appointment();
    private User user0 = new User();

    @BeforeEach
    public void setUp() throws Exception {
        //  Create a user
        user0.setId(1L);
        user0.setName("Josefino");
        user0.setEmail("jose@fino.com");
        user0.setPhone("919828737");

        //  Create a appointment
        app0.setId(1L);
        app0.setSpeciality(specialityService.getSpecialityById(1));
        app0.setDate(new Date());
        app0.setPrice(12.3);
        app0.setPatient(user0);
        

        when(service.save(Mockito.any())).thenReturn(app0);
        when(service.getAppointmentsByPatient(user0)).thenReturn(List.of(app0));
        when(service.getAppointmentById(app0.getId())).thenReturn(app0);

        when(userService.getUserFromContext()).thenReturn(user0);
        
        when(specialityService.getSpecialityById(1)).thenReturn(Speciality.PULMONOLOGY);
    }

    @Test
    void whenPostValidAppointment_thenCreateAppointment() throws Exception {
        mvc.perform(
                post("/appointments").contentType(MediaType.APPLICATION_JSON)
                .content("{\"date\": \"2024-04-26T18:32:09\"" +
                         ",\"price\":" + app0.getPrice().toString() +
                         ",\"specialityId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(app0.getPrice())));

        verify(service, times(1)).save(Mockito.any());

    }

    @Test
    void givenOneAppointments_thenReturnIt() throws Exception {

        mvc.perform(
                get("/appointments/" + app0.getId().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(app0.getPrice())));

        verify(service, times(1)).getAppointmentById(Mockito.any());
    }

    @Test
    void givenManyAppointments_thenReturnInJsonArray() throws Exception {
        Appointment appointment0 = new Appointment();
        Appointment appointment1 = new Appointment();
        Appointment appointment2 = new Appointment();

        appointment0.setPrice(10.72);
        appointment1.setPrice(16.98);
        appointment2.setPrice(23.53);

        List<Appointment> allAppointments = Arrays.asList(appointment0, appointment1, appointment2);

        when(service.getAppointmentsByPatient(user0)).thenReturn(allAppointments);

        mvc.perform(
                get("/appointments").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].price", is(appointment0.getPrice())))
                .andExpect(jsonPath("$[1].price", is(appointment1.getPrice())))
                .andExpect(jsonPath("$[2].price", is(appointment2.getPrice())));

        verify(service, times(1)).getAppointmentsByPatient(user0);
    }

    //  Tests for bad conditions
/*     @Test
    void givenNullValue_whenAdd_thenReturnError() throws Exception {
        //  Check null firstname
        mvc.perform(
                post("/appointments/buy").contentType(MediaType.APPLICATION_JSON)
                .param("firstname", "")
                .param("lastname", appointment0.getLastname())
                .param("phone", appointment0.getPhone())
                .param("email", appointment0.getEmail())
                .param("creditCard", appointment0.getCreditCard())
                .param("numberOfPeople", appointment0.getNumberOfPeople().toString())
                .param("seatNumber", appointment0.getSeatNumber().toString())
                .param("trip", appointment0.getTrip().getId().toString())
                .param("currency", appointment0.getCurrency()))
                .andExpect(status().isUnprocessableEntity());

        verify(service, times(0)).save(Mockito.any());

    } */
}