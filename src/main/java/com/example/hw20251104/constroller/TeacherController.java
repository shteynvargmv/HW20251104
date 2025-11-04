package com.example.hw20251104.constroller;

import com.example.hw20251104.model.BulkAddResponse;
import com.example.hw20251104.model.ErrorResponse;
import com.example.hw20251104.model.Teacher;
import com.example.hw20251104.model.TeacherDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/teacher")
public class TeacherController {

    List<Teacher> teachers = new ArrayList<>();

    @GetMapping("/all")
    public ResponseEntity<?> all() {
        if (teachers.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.NO_CONTENT, "список пустой");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(teachers);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> id(
            @PathVariable Integer id
    ) {
        List<String> validationErrors = TeacherDTO.validationId(id);
        if (!validationErrors.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                    "ошибки валидации", validationErrors);
        }
        Optional<Teacher> isResult = teachers.stream().filter(p -> p.getId().equals(id)).findFirst();

        if (isResult.isPresent()) {
            Teacher teacher = isResult.get();
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Location", "/api/teacher/" + teacher.getId())
                    .body(teacher);
        } else {
            return getErrorEntityResponse(HttpStatus.NOT_FOUND, "учитель не найден");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName
    ) {
        List<String> validationErrors = TeacherDTO.validationSearch(firstName, lastName);
        if (!validationErrors.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                    "некорректные параметры", validationErrors);
        }

        List<Teacher> result = teachers;

        if (firstName != null && !firstName.isEmpty()) {
            result = result.stream().filter(x -> x.getFirstName().equalsIgnoreCase(firstName)).toList();
        }

        if (lastName != null && !lastName.isEmpty()) {
            result = result.stream().filter(x -> x.getLastName().equalsIgnoreCase(lastName)).toList();
        }

        if (result.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.NO_CONTENT, "ничего не найдено");

        } else {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }

    }

    @GetMapping("/subject/{subject}")
    public ResponseEntity<?> subject(
            @PathVariable String subject
    ) {
        List<Teacher> result = teachers.stream().filter(x -> x.getSubject().equalsIgnoreCase(subject)).toList();

        if (result.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.NO_CONTENT, "ничего не найдено");

        } else {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @GetMapping("/subject/")
    public ResponseEntity<?> subject(
    ) {
        List<String> validationErrors = TeacherDTO.validationSubject("");
        return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                "некорректные параметры", validationErrors);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> subject(
            @RequestParam(required = false) Integer minExp,
            @RequestParam(required = false) Integer maxExp,
            @RequestParam(required = false) Double minSalary,
            @RequestParam(required = false) Double maxSalary
    ) {

        List<String> validationErrors = new ArrayList<>();
        if (minExp != null && maxExp != null &&
                minExp.compareTo(maxExp) > 0) {
            validationErrors.add("Error: minExp > maxExp");
        }
        if (minSalary != null && maxSalary != null &&
                minSalary.compareTo(maxSalary) > 0) {
            validationErrors.add("Error: minSalary > maxSalary");
        }
        if (!validationErrors.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                    "ошибки валидации", validationErrors);
        }

        List<Teacher> result = teachers;

        if (minExp != null) {
            result = result.stream().filter(x -> x.getExperience() >= minExp).toList();
        }

        if (maxExp != null) {
            result = result.stream().filter(x -> x.getExperience() <= maxExp).toList();
        }

        if (minSalary != null) {
            result = result.stream().filter(x -> x.getSalary() >= minSalary).toList();
        }
        if (maxSalary != null) {
            result = result.stream().filter(x -> x.getSalary() <= maxSalary).toList();
        }

        if (result.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.NO_CONTENT, "ничего не найдено");

        } else {
            return ResponseEntity.status(HttpStatus.OK).body(result);
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> active() {
        List<Teacher> result = teachers.stream().filter(x -> x.getActive() == Boolean.TRUE).toList();
        if (result.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.NO_CONTENT, "нет активных учителей");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(teachers);
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Integer> count() {
        Integer result = teachers.size();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/count-by-subject")
    public ResponseEntity<?> countBySubject() {

        if (teachers.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.NO_CONTENT, "нет учителей");

        } else {
            Map<String, Integer> stat = new HashMap<>();
            for (Teacher teacher : teachers) {
                stat.merge(teacher.getSubject(), 1, Integer::sum);
            }
            return ResponseEntity.status(HttpStatus.OK).body(stat);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(
            @RequestBody TeacherDTO teacher
    ) {
        return addTeacher(teacher);
    }

    private ResponseEntity<?> addTeacher(
            TeacherDTO teacher
    ) {
        List<String> validationErrors = TeacherDTO.validationAdd(teacher);

        if (teachers.stream().anyMatch(x -> x.getFirstName().equalsIgnoreCase(teacher.getFirstName()) &&
                x.getLastName().equalsIgnoreCase(teacher.getLastName()))) {

            validationErrors.add("Teacher with this name already exists");
            return getErrorEntityResponse(HttpStatus.CONFLICT,
                    "учитель с таким именем уже существует", validationErrors);
        }

        if (!validationErrors.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                    "ошибки валидации", validationErrors);
        }

        Teacher teacherNew = new Teacher(teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getExperience(),
                teacher.getSubject(),
                teacher.getSalary(),
                teacher.getEmail(),
                teacher.getActive());
        teachers.add(teacherNew);

        return ResponseEntity.status(HttpStatus.CREATED)
                .header("Location", "/api/teacher/" + teacherNew.getId())
                .body(teacherNew);
    }

    @PostMapping("/add-bulk")
    public ResponseEntity<?> add(
            @RequestBody List<TeacherDTO> teacherList
    ) {

        BulkAddResponse result = new BulkAddResponse();
        for (TeacherDTO teacher : teacherList) {
            ResponseEntity<?> response = addTeacher(teacher);
            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                result.resultAdd();
            } else {
                List<String> errors = new ArrayList<>();
                ErrorResponse er = (ErrorResponse) response.getBody();
                if (er != null) {
                    errors = er.getErrors();
                }
                System.out.println(response.getBody());
                result.resultAdd(teacher.getFirstName() + " " + teacher.getLastName(),
                        errors);
            }
        }

        if (result.getAdded() == 0 && result.getFailed() != 0) {
            //400 BAD REQUEST - все записи невалидны
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result);

        } else if (result.getAdded() != 0 && result.getFailed() == 0) {
            //201 CREATED - все учителя добавлены
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(result);

        } else if (result.getAdded() != 0 && result.getFailed() != 0) {
            //207 MULTI-STATUS - часть добавлена, часть нет
            return ResponseEntity.status(HttpStatus.MULTI_STATUS)
                    .body(result);

        } else { //(result.getAdded() == 0 && result.getFailed() == 0){
            //400 BAD REQUEST - все записи невалидны
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(result);
        }

    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Integer id,
            @RequestBody TeacherDTO teacher
    ) {
        List<String> validationErrors = TeacherDTO.validationUpdateById(id, teacher);
        if (!validationErrors.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                    "ошибки валидации", validationErrors);
        }

        if (teachers.stream().anyMatch(t -> !t.getId().equals(id) &&
                t.getFirstName().equalsIgnoreCase(teacher.getFirstName()) &&
                t.getLastName().equalsIgnoreCase(teacher.getLastName())
        )) {
            return getErrorEntityResponse(HttpStatus.CONFLICT,
                    "конфликт имени с другим учителем", new ArrayList<>() {{
                        add("Teacher with this name already exists");
                    }});
        }

        Optional<Teacher> isResult = teachers.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (isResult.isPresent()) {
            Teacher teacherById = isResult.get();
            teacherById.setFirstName(teacher.getFirstName());
            teacherById.setLastName(teacher.getLastName());
            teacherById.setSubject(teacher.getSubject());
            teacherById.setExperience(teacher.getExperience());
            teacherById.setSalary(teacher.getSalary());
            teacherById.setEmail(teacher.getEmail());
            teacherById.setActive(teacher.getActive());
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Location", "/api/teacher/" + teacherById.getId())
                    .body(teacherById);
        } else {
            return getErrorEntityResponse(HttpStatus.NOT_FOUND, "учитель не найден");
        }
    }

    @PatchMapping("/update-partial/{id}")
    public ResponseEntity<?> updatePartial(
            @PathVariable Integer id,
            @RequestBody TeacherDTO teacher
    ) {
        List<String> validationErrors = TeacherDTO.validationUpdatePartialById(id, teacher);
        if (!validationErrors.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                    "ошибки валидации", validationErrors);
        }

        if (teacher.getFirstName() != null && teacher.getLastName() != null &&
                teachers.stream().anyMatch(t -> !t.getId().equals(id) &&
                        t.getFirstName().equalsIgnoreCase(teacher.getFirstName()) &&
                        t.getLastName().equalsIgnoreCase(teacher.getLastName())
                )) {
            return getErrorEntityResponse(HttpStatus.CONFLICT,
                    "конфликт имени с другим учителем", new ArrayList<>() {{
                        add("Teacher with this name already exists");
                    }});
        }

        Optional<Teacher> isResult = teachers.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (isResult.isPresent()) {
            Teacher teacherById = updateTeacher(teacher, isResult);
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Location", "/api/teacher/" + teacherById.getId())
                    .body(teacherById);
        } else {
            return getErrorEntityResponse(HttpStatus.NOT_FOUND, "учитель не найден");
        }
    }

    private static Teacher updateTeacher(TeacherDTO teacher, Optional<Teacher> isResult) {
        Teacher teacherById = isResult.get();
        if (teacher.getFirstName() != null) {
            teacherById.setFirstName(teacher.getFirstName());
        }
        if (teacher.getLastName() != null) {
            teacherById.setLastName(teacher.getLastName());
        }
        if (teacher.getSubject() != null) {
            teacherById.setSubject(teacher.getSubject());
        }
        if (teacher.getExperience() != null) {
            teacherById.setExperience(teacher.getExperience());
        }
        if (teacher.getSalary() != null) {
            teacherById.setSalary(teacher.getSalary());
        }
        if (teacher.getEmail() != null) {
            teacherById.setEmail(teacher.getEmail());
        }
        if (teacher.getActive() != null) {
            teacherById.setActive(teacher.getActive());
        }
        return teacherById;
    }

    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivate(
            @PathVariable Integer id
    ) {
        List<String> validationErrors = TeacherDTO.validationId(id);
        if (!validationErrors.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                    "ошибки валидации", validationErrors);
        }

        Optional<Teacher> isResult = teachers.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (isResult.isPresent()) {
            Teacher teacherById = isResult.get();

            if (teacherById.getActive() == false) {
                validationErrors.add("Teacher is already deactivated");
                return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                        "ошибки валидации", validationErrors);
            } else {
                teacherById.setActive(false);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Location", "/api/teacher/" + teacherById.getId())
                    .body(teacherById);
        } else {
            return getErrorEntityResponse(HttpStatus.NOT_FOUND, "учитель не найден");
        }
    }

    @PatchMapping("/activate/{id}")
    public ResponseEntity<?> activate(
            @PathVariable Integer id
    ) {
        List<String> validationErrors = TeacherDTO.validationId(id);
        if (!validationErrors.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                    "ошибки валидации", validationErrors);
        }

        Optional<Teacher> isResult = teachers.stream().filter(t -> t.getId().equals(id)).findFirst();
        if (isResult.isPresent()) {
            Teacher teacherById = isResult.get();

            if (teacherById.getActive() == true) {
                validationErrors.add("Teacher is already activated");
                return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                        "ошибки валидации", validationErrors);
            } else {
                teacherById.setActive(true);
            }
            return ResponseEntity.status(HttpStatus.OK)
                    .header("Location", "/api/teacher/" + teacherById.getId())
                    .body(teacherById);
        } else {
            return getErrorEntityResponse(HttpStatus.NOT_FOUND, "учитель не найден");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Integer id
    ) {
        List<String> validationErrors = TeacherDTO.validationId(id);
        if (!validationErrors.isEmpty()) {
            return getErrorEntityResponse(HttpStatus.BAD_REQUEST,
                    "ошибки валидации", validationErrors);
        }

        boolean result = teachers.removeIf(t -> t.getId().equals(id));
        if (result) {
            return getErrorEntityResponse(HttpStatus.NO_CONTENT, "успешное удаление");
        } else {
            return getErrorEntityResponse(HttpStatus.NOT_FOUND, "учитель не найден");
        }
    }

    @DeleteMapping("/delete-inactive")
    public ResponseEntity<?> deleteInactive() {
        boolean result = teachers.removeIf(t -> t.getActive().equals(false));
        if (result) {
            return getErrorEntityResponse(HttpStatus.OK, "неактивные учителя удалены");
        } else {
            return getErrorEntityResponse(HttpStatus.NO_CONTENT, "нет неактивных учителей");
        }
    }

    private static ResponseEntity<?> getErrorEntityResponse(HttpStatus status, String message, List<String> errors) {
        ErrorResponse response = ErrorResponse.badRequest(status.value(), status.getReasonPhrase(), message, errors);
        return ResponseEntity
                .status(status)
                .body(response);
    }

    private static ResponseEntity<?> getErrorEntityResponse(HttpStatus status, String message) {
        ErrorResponse response = ErrorResponse.badRequest(status.value(), status.getReasonPhrase(), message);
        return ResponseEntity
                .status(status)
                .body(response);
    }
}