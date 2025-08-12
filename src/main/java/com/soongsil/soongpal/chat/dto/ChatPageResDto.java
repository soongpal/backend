package com.soongsil.soongpal.chat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChatPageResDto<T> {

    private List<T> content;

    @Schema(description = "현재 페이지 번호")
    private int currentPage;

    @Schema(description = "총 페이지 수")
    private int totalPages;

    private boolean first;
    private boolean last;

    public static <T> ChatPageResDto<T> from(Page<T> page) {
        return ChatPageResDto.<T>builder()
                .content(page.getContent())
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }
}