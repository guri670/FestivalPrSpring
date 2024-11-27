package com.myproject.myapp.persistance;

import java.util.ArrayList;

import com.myproject.myapp.domain.CommentVo;

public interface CommentMapper {

	public ArrayList<CommentVo> commentSelectAll(int bidx, int block);
	public int commentInsert(CommentVo cv);
	public int commentDelete(CommentVo cv);
	public int commentTotalCnt(int bidx);
}