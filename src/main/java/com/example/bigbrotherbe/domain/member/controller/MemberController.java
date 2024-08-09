package com.example.bigbrotherbe.domain.member.controller;

import com.example.bigbrotherbe.domain.member.entity.dto.request.SignUpDto;
import com.example.bigbrotherbe.domain.member.entity.dto.response.MemberResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RequestMapping("/api/big-brother/members")
@Tag(name = "멤버", description = "회원가입,로그인 API")
public interface MemberController {

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/sign-up")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "Success",
            content = {@Content(schema = @Schema(implementation = MemberResponse.class))}),
        @ApiResponse(responseCode = "404", description = "?")
    })
    ResponseEntity<com.example.bigbrotherbe.global.response.ApiResponse<MemberResponse>> signUp(@RequestBody @Valid SignUpDto signUpDto);
}
