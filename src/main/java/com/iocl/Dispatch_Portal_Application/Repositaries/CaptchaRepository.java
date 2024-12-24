package com.iocl.Dispatch_Portal_Application.Repositaries;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iocl.Dispatch_Portal_Application.Entity.CaptchaEntity;

public interface CaptchaRepository extends JpaRepository<CaptchaEntity, UUID> {

	Optional<CaptchaEntity> findByValue(String captcha_value);

}
