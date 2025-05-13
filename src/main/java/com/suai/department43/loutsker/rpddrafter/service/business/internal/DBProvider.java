package com.suai.department43.loutsker.rpddrafter.service.business.internal;

import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Account;
import com.suai.department43.loutsker.rpddrafter.domain.entity.auth.Role;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.DisciplineEntity;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.RPDEntity;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.Teacher;
import com.suai.department43.loutsker.rpddrafter.domain.entity.business.persistent.UniversityPosition;
import com.suai.department43.loutsker.rpddrafter.exception.business.DisciplineNotFoundException;
import com.suai.department43.loutsker.rpddrafter.exception.business.TeacherNotFoundException;
import com.suai.department43.loutsker.rpddrafter.repository.auth.AccountRepository;
import com.suai.department43.loutsker.rpddrafter.repository.business.DisciplineRepository;
import com.suai.department43.loutsker.rpddrafter.repository.business.PositionRepository;
import com.suai.department43.loutsker.rpddrafter.repository.auth.RoleRepository;
import com.suai.department43.loutsker.rpddrafter.repository.business.RPDRepository;
import com.suai.department43.loutsker.rpddrafter.repository.business.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DBProvider {
    private final DisciplineRepository disciplineRepo;
    private final TeacherRepository teacherRepo;
    private final PositionRepository positionRepository;
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final RPDRepository rpdRepository;

    public DBProvider(DisciplineRepository disciplineRepo, TeacherRepository teacherRepo,
                      PositionRepository positionRepository, RoleRepository roleRepository,
                      AccountRepository accountRepository, RPDRepository rpdRepository) {
        this.disciplineRepo = disciplineRepo;
        this.teacherRepo = teacherRepo;
        this.positionRepository = positionRepository;
        this.roleRepository = roleRepository;
        this.accountRepository = accountRepository;
        this.rpdRepository = rpdRepository;
    }

    public void saveRPD(RPDEntity rpd) {
        rpdRepository.save(rpd);
    }

    public RPDEntity getRPDById(long id) {
        Optional<RPDEntity> rpdVersion = rpdRepository.findById(id);
        if (rpdVersion.isEmpty()) {
            throw new RuntimeException("RPD with '" + id + "' +not found");
        }
        return rpdVersion.get();
    }

    public List<RPDEntity> getAllRPDs() {
        List<RPDEntity> rpds = rpdRepository.findAll();
        rpds.forEach(rpd -> {
            rpd.setBody(null);
        });
        return rpds;
    }

    public List<RPDEntity> getRPDByProperties(String disciplineName, String programCode, int enrollYear, String authorName) {
        if (!Objects.equals(disciplineName, "")) {
            disciplineName = "";
        }
        if (!Objects.equals(programCode, "")) {
            programCode = "";
        }
        if (!Objects.equals(authorName, "")) {
            authorName = "";
        }
        return rpdRepository.findByProperties(disciplineName, programCode, enrollYear, authorName);
    }

    public void deleteRPDById(long id) {
        rpdRepository.deleteById(id);
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public Role getRoleByName(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isEmpty()) {
            throw new RuntimeException(name);
        }
        return role.get();
    }

    public void deleteTeacherById(long id) {
        teacherRepo.deleteById(id);
    }

    public void saveTeacher(Teacher teacher) {
        teacherRepo.save(teacher);
    }

    public void savePosition(UniversityPosition position) {
        positionRepository.save(position);
    }

    public List<DisciplineEntity> getDisciplines() {
        return disciplineRepo.findAll();
    }

    public List<Teacher> getTeachers() {
        return teacherRepo.findAll();
    }

    public DisciplineEntity getDisciplineById(long id) {
        Optional<DisciplineEntity> discipline = disciplineRepo.findById(id);
        if (discipline.isEmpty()) {
            throw new DisciplineNotFoundException(id);
        }
        return discipline.get();
    }

    public Teacher getTeacherById(long id) {
        Optional<Teacher> teacher = teacherRepo.findById(id);
        if (teacher.isEmpty()) {
            throw new TeacherNotFoundException(id);
        }
        return teacher.get();
    }

    public void saveDisciplineList(Iterable<DisciplineEntity> disciplines) {
        disciplineRepo.deleteAll();
        disciplineRepo.saveAll(disciplines);
    }

    public boolean teacherHasAccount(Teacher teacher) {
        return !accountRepository.findAccountsByBearer(teacher).isEmpty();
    }
}
