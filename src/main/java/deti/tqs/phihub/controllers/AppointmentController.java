package deti.tqs.phihub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import deti.tqs.phihub.models.Appointment;

import deti.tqs.phihub.services.AppointmentService;

import deti.tqs.phihub.services.SpecialityService;
import deti.tqs.phihub.services.UserService;
import deti.tqs.phihub.dtos.AppointmentSchema;


@RestController
@RequestMapping("/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    private UserService userService;

    private SpecialityService specialityService;


    @Autowired
    public AppointmentController(AppointmentService appointmentService, UserService userService,
            SpecialityService specialityService) {
        this.appointmentService = appointmentService;
        this.userService = userService;
        this.specialityService = specialityService;
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentSchema appointmentSchema) {
        var user = userService.getUserFromContext();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var speciality = specialityService.getSpecialityById(appointmentSchema.specialityId());
        if (speciality == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        var appointment = appointmentService.createAppointment(user, speciality, appointmentSchema.date(),
                appointmentSchema.price());
        return ResponseEntity.ok(appointment);
    }

    /*
     * @GetMapping
     * public ResponseEntity<List<Appointment>> getAppointments() {
     * var user = userService.getUserFromContext();
     * var appointments = appointmentService.getAppointmentsByPatient(user);
     * return ResponseEntity.ok(appointments);
     * }
     * 
     * 
     * @GetMapping("/{id}")
     * public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
     * var user = userService.getUserFromContext();
     * var appointment = appointmentService.getAppointmentById(id);
     * if (appointment.getPatient().getId() != user.getId()) {
     * return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
     * }
     * return ResponseEntity.ok(appointment);
     * }
     * 
     * @DeleteMapping("/{id}")
     * public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
     * var user = userService.getUserFromContext();
     * var appointment = appointmentService.getAppointmentById(id);
     * if (appointment.getPatient().getId() != user.getId()) {
     * return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
     * }
     * appointmentService.deleteAppointment(appointment);
     * return ResponseEntity.ok().build();
     * }
     * 
     * @PostMapping("/appointments/{id}/pay")
     * public ResponseEntity<Bill> payAppointment(@PathVariable Long id) {
     * var user = userService.getUserFromContext();
     * var appointment = appointmentService.getAppointmentById(id);
     * if (appointment.getPatient().getId() != user.getId()) {
     * return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
     * }
     * var bill = billService.createBill(appointment);
     * // ... rest of the logic for paying the appointment
     * }
     */

}