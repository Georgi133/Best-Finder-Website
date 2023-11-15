import { useState } from "react";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useTorrentContext } from "../TorrentContext.js/TorrentContext";
import { useForm } from "../useForm/useForm";
import style from "./Details.module.css";

export const Comment = ({
  commentId,
  torrentId,
  torrent,
  fullName,
  commentText,
  emailOfUserOwnerOfTheComment,
}) => {


  const [isEditClicked,setIsEditClicked] = useState(false);
  const [editCommentMessage,setEditCommentMessage] = useState('')

  const { values, changeHandler } = useForm({
    comment: commentText,
  });

  const { userEmail, userRole } = useAuthContext();
  const { onDeleteComment,editLocalById } = useTorrentContext();

  const onEditClick = (e) => {
    setIsEditClicked(true);
  }

  const onEditOkClick = () => {
    if(values.comment.length === 0) {
      setEditCommentMessage('Must be at least 1 character');
      return;
    }
    setEditCommentMessage('');
    editLocalById(torrentId, commentId, torrent, values.comment);
    setIsEditClicked(false);
  }

  return (
    <div className={style.commentInnerContainer}>
      { editCommentMessage && <div className={style.commentValidate}>{editCommentMessage}</div> }
      <div className={style.email}>
        <p>{fullName} :</p>
      </div>
      {isEditClicked &&
      <form className={style.formContainer}>
          <label htmlFor="comment">Editing: </label>
          <textarea
            className={style.editArea}
            value={values.comment}
            onChange={changeHandler}
            id="comment"
            name="comment"
            type="text"
          ></textarea>
          <button onClick={onEditOkClick} className={style.userBtnOk} type="button">
            Ok
          </button>
        </form>}
        {!isEditClicked &&
      <div className={style.comment}>
        <p>{commentText}</p>
      </div> }
      {(emailOfUserOwnerOfTheComment === userEmail || userRole === 'ADMIN') 
      && !isEditClicked 
      && (
      <div>
          <button onClick={onEditClick} className={style.userBtn} type="button">
            Edit
          </button>
          <button onClick={() => onDeleteComment(torrentId, commentId, torrent, userEmail)} className={style.userBtn} type="button">
            Delete
          </button>
        </div>
       )}
    </div>
  );
};
