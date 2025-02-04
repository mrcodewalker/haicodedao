package com.example.codewalker.kma.repositories;

import com.example.codewalker.kma.models.HocPhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HocPhanRepository extends JpaRepository<HocPhan, Long> {
}
