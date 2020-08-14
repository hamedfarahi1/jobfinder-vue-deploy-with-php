package ir.khu.jaobshaar.component.user;

import ir.khu.jaobshaar.entity.enums.PersonRuleType;
import ir.khu.jaobshaar.entity.model.Employer;
import ir.khu.jaobshaar.entity.model.Job;
import ir.khu.jaobshaar.repository.EmployerRepository;
import ir.khu.jaobshaar.service.domain.JobDomain;
import ir.khu.jaobshaar.service.dto.user.UserDTO;
import ir.khu.jaobshaar.service.mapper.JobMapper;
import ir.khu.jaobshaar.utils.ValidationUtils;
import ir.khu.jaobshaar.utils.validation.ErrorCodes;
import ir.khu.jaobshaar.utils.validation.ResponseException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;


@Service
public class EmployerManager {

    private EmployerRepository employerRepository;

    private PasswordEncoder bcryptEncoder;
    private final JobMapper jobMapper;
    private final UserManager userManager;

    public EmployerManager(EmployerRepository employerRepository, PasswordEncoder bcryptEncoder, JobMapper jobMapper, UserManager userManager) {
        this.employerRepository = employerRepository;
        this.bcryptEncoder = bcryptEncoder;
        this.jobMapper = jobMapper;
        this.userManager = userManager;
    }

    @Transactional
    public void login(final UserDTO userDTO) {
        ValidationUtils.validateUser(userDTO);

        final Employer employer = employerRepository.findByUsername(userDTO.getUsername());

        if (employer == null) {
            throw ResponseException.newResponseException(
                    ErrorCodes.ERROR_CODE_USER_NOT_FOUND, " ERROR_CODE_USER_NOT_FOUND "
            );
        }
    }

    @Transactional
    public void register(final UserDTO userDTO) {
        ValidationUtils.validateUser(userDTO);

        if (ValidationUtils.isInvalidEmailAddress(userDTO.getEmail())) {
            throw ResponseException.newResponseException(
                    ErrorCodes.ERROR_CODE_INVALID_EMAIL, " Invalid Email"
            );
        }


        if (employerRepository.findByUsername(userDTO.getUsername()) != null) {
            throw ResponseException.newResponseException(
                    ErrorCodes.ERROR_CODE_USER_ALREADY_EXIST, " ERROR_CODE_USER_ALREADY_EXIST "
            );
        }

        if (employerRepository.findByEmail(userDTO.getEmail()) != null) {
            throw ResponseException.newResponseException(
                    ErrorCodes.ERROR_CODE_EMAIL_ALREADY_EXIST, " ERROR_CODE_EMAIL_ALREADY_EXIST "
            );
        }

        employerRepository.save(
                new Employer(
                        userDTO.getUsername(),
                        bcryptEncoder.encode(userDTO.getPassword()),
                        userDTO.getEmail(),
                        PersonRuleType.EMPLOYER
                )
        );
    }

    public JobDomain getOne(long id) {
        Set<Job> jobs = employerRepository.findByUsername(userManager.getCurrentUser().getUsername()).getJobs();
        if (jobs != null) {
            Optional<Job> optionalJob = jobs.stream().filter(job -> job.getId().equals(id)).findFirst();
            if (optionalJob.isPresent())
                return jobMapper.toDomain(optionalJob.get());
        }
        return null;
    }
}
