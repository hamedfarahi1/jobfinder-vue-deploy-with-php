package ir.khu.jaobshaar.service.user;

import ir.khu.jaobshaar.component.user.UserManager;
import ir.khu.jaobshaar.service.dto.user.ChangePasswordDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/account")
public class AccountController {

    private final UserManager userManager;

    public AccountController(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping
    public ResponseEntity<?> getCurrentAccount() {
        return ResponseEntity.ok(userManager.getCurrentUser());
    }

    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPassword(String username) {
        userManager.forgetPassWord(username);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userManager.resetPassword(changePasswordDTO);
        return ResponseEntity.ok("ok");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        userManager.resetPassword(changePasswordDTO);
        return ResponseEntity.ok("ok");
    }
}
