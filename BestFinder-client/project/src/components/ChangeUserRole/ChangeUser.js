import style from "./ChangeUserRole.module.css";
import { useState } from "react";
import { useAdminContext } from "../AdminContext/AdminContext";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useTranslation } from "react-i18next";
import { ErrorMessage } from "../ErrorMessage/ErrorMessage";

export const ChangeUser = ({ 
    setChangeRole,
    currentUserRole,

 }) => {
  const { t } = useTranslation();
  const { onClickChangeRole, foundUserEmail, errorMessageAdmin } = useAdminContext();
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
              {t("changingRole.title")}
              </h1>
              {errorMessageAdmin && <ErrorMessage message={errorMessageAdmin} />}

              <form 
                className={style.formRegPass} 
                >
                <select
                  value={currentRole}
                  onChange={onRoleChange}
                  className={style.changeOpt}
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
                  {t("changingRole.btn")}
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};
