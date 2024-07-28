package com.example.bigbrotherbe.global.file.repository;

import com.example.bigbrotherbe.global.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
