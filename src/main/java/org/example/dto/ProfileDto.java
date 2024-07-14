package org.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.dto.member.MemberDto;

@Data
@RequiredArgsConstructor
public class ProfileDto {
    private boolean follow;
    private MemberDto memberDto;

    @Builder
    public ProfileDto(boolean follow, MemberDto memberDto){
        this.follow=follow;
        this.memberDto=memberDto;
    }
}
