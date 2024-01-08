package com.community.amkorea.bookmark.service;

import static com.community.amkorea.global.exception.ErrorCode.ALREADY_REGISTERED;
import static com.community.amkorea.global.exception.ErrorCode.BOOKMARK_NOT_FOUND;
import static com.community.amkorea.global.exception.ErrorCode.TEAM_NOT_FOUND;
import static com.community.amkorea.global.exception.ErrorCode.USER_NOT_FOUND;

import com.community.amkorea.bookmark.dto.BookmarkRequest;
import com.community.amkorea.bookmark.dto.BookmarkResponse;
import com.community.amkorea.bookmark.entity.Bookmark;
import com.community.amkorea.bookmark.repository.BookmarkRepository;
import com.community.amkorea.global.exception.CustomException;
import com.community.amkorea.member.entity.Member;
import com.community.amkorea.member.repository.MemberRepository;
import com.community.amkorea.soccer.entity.Team;
import com.community.amkorea.soccer.repository.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

  private final BookmarkRepository bookmarkRepository;
  private final MemberRepository memberRepository;
  private final TeamRepository teamRepository;

  @Override
  public BookmarkResponse createBookmark(String username, BookmarkRequest request) {
    if(bookmarkRepository.existsByTeamId(request.getTeamId())) {
      throw new CustomException(ALREADY_REGISTERED);
    }

    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    Team team = teamRepository.findById(request.getTeamId())
        .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

    Bookmark bookmark = request.toEntity();
    bookmark.addMember(member);
    bookmark.addTeam(team);

    return BookmarkResponse.fromEntity(bookmarkRepository.save(bookmark));
  }

  @Override
  @Transactional(readOnly = true)
  public List<BookmarkResponse> findBookmark(String username) {
    Member member = memberRepository.findByEmail(username)
        .orElseThrow(() -> new CustomException(USER_NOT_FOUND));

    return bookmarkRepository.findAllByMember(member).stream()
        .map(BookmarkResponse::fromEntity).toList();
  }

  @Override
  @Transactional
  public void deleteBookmark(String username, Long id) {
    Bookmark bookmark = bookmarkRepository.findById(id)
        .orElseThrow(() -> new CustomException(BOOKMARK_NOT_FOUND));

    bookmarkRepository.delete(bookmark);
  }

}
