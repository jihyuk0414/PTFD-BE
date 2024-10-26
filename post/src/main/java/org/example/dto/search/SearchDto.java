package org.example.dto.search;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SearchDto {
    private String word;
    private String post_name;
}
