package com.providences.events.user;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.providences.events.config.token.RefreshTokenEntity;
import com.providences.events.organizer.OrganizerEntity;
import com.providences.events.supplier.SupplierEntity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 40)
    private String id;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 15, nullable = false)
    private String phone;

    @JsonIgnore
    @Column(length = 70, nullable = false)
    private String passwordHash;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 11)
    private ROLE role;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private SupplierEntity supplier;

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private OrganizerEntity organizer;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<RefreshTokenEntity> refreshTokens;

    // //////////////////
    @PrePersist
    protected void onCreate() {
        role = ROLE.CLIENT;
        isDeleted = false;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ROLE {
        CLIENT,
        ADMIN
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == ROLE.ADMIN) {
            return Set.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("CLIENT"));
        } else {
            return Set.of(new SimpleGrantedAuthority("CLIENT"));
        }
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String toString() {
        return "UserEntity [id=" + id + ", name=" + name + ", email=" + email + ", phone=" + phone + ", passwordHash="
                + passwordHash + ", isDeleted=" + isDeleted + ", createdAt="
                + createdAt + ", updatedAt=" + updatedAt + ", role=" + role + "]";
    }

}
