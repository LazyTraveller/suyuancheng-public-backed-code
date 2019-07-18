package com.suyuancheng.backend.api.rest

import com.suyuancheng.backend.domain.Role
import com.suyuancheng.backend.domain.User
import com.suyuancheng.backend.service.UserService
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

/**
 * @author hsj
 */
@RestController
@RequestMapping("api/rest/user")
class UserController(
    private val userService: UserService
) {
    data class UserDTO(
        val username: String,
        val password: String,
        val realName: String
    )

    @GetMapping("/user-info")
    fun getUserInfo(
        @AuthenticationPrincipal user: User
    ): User = user

    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR')")
    @GetMapping("/")
    fun query(
        @RequestParam(required = false) realName: String?,
        @PageableDefault(
            sort = ["id"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ) = userService.query(realName, pageable)

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Int,
        @RequestBody roles: MutableSet<Role>
    ) = userService.update(id, roles)

    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    @PostMapping("/")
    fun create(
        @RequestBody userDTO: UserDTO
    ) = userService.create(
        userDTO.username,
        userDTO.password,
        userDTO.realName
    )
}