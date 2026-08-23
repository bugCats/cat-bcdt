package cc.bugcat.bcdt.ops.openapi.note.user.day.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@Schema
public class UserDayNoteVo {
    
    @Schema(description = "最后保存时间")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date lastSavedate;
    
    @Schema(description = "日报内容")
    private List<UserDayNote> list;


}
