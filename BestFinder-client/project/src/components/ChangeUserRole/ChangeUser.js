import style from "./ChangeUserRole.module.css";
import { useState } from "react";
import { useAdminContext } from "../AdminContext/AdminContext";
import { useAuthContext } from "../AuthContext/AuthContext";

export const ChangeUser = ({ 
    setChangeRole,
    currentUserRole,

 }) => {
  const { onClickChangeRole, foundUserEmail } = useAdminContext();
  const { userRole } = useAuthContext();

  const [currentRole, setCurrentRole] = useState("role");

  const onRoleChange = (e) => {
    const value = e.target.value;
    setCurrentRole(value)
  }


  return (
    <div className={style.overlay}>
      <div className={style.backdrop}></div>
      <div className={style.modal}>
        <div className={style.detailContainer}>
          <button
            className={style.btn2}
            type="submit"
            onClick={() => setChangeRole(false)}
          >
            X
          </button>
          <div className={style.content}>
            <div className={style.userDetails}>
              <h1 className={style.regenerationPass}>
                Change Role
              </h1>

              <form 
                className={style.formRegPass} 
                >
                <select
                  value={currentRole}
                  onChange={onRoleChange}
                  className={style.selectContainer + " " + style.changeOpt}
                >
                    <option className={style.opt} hidden value="role">
                    ROLE
                  </option>
                  { currentUserRole === 'ADMIN' &&
                  <option className={style.opt} value="user">
                    USER
                  </option>
                     }
                     { currentUserRole === 'USER' &&
                  <option className={style.opt} value="admin">
                    ADMIN
                  </option>
                     }
                </select>

                <button onClick={() => onClickChangeRole({role : userRole, 
                changeUserRole : currentRole.toUpperCase(), 
                email : foundUserEmail, setChangeRole})}  type="button" className={style.editBtn}>
                  Change
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
