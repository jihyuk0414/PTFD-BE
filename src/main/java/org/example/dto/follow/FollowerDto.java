package org.example.dto.follow;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.dto.member.MemberDto;


import java.util.List;

@Data
@RequiredArgsConstructor
public class FollowerDto {
    private List<MemberDto> followers;
    @Builder
    public FollowerDto(List<MemberDto> followers){
        this.followers=followers;
    }

}
