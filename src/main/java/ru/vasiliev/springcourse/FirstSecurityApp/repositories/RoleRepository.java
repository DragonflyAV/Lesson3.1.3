package ru.vasiliev.springcourse.FirstSecurityApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vasiliev.springcourse.FirstSecurityApp.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
