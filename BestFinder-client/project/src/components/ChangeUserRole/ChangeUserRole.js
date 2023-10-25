import { MyNavBar } from "../Header/MyNavBar";
import { useForm } from "../useForm/useForm";
import style from "./ChangeUserRole.module.css";
import { ButtonSubmit } from "../Login/ButtonSubmit";
import { useAdminContext } from "../AdminContext/AdminContext";
import { useAuthContext } from "../AuthContext/AuthContext";
import { useState } from "react";
import { ChangeUser } from "./ChangeUser";

export const ChangeUserRole = () => {

    const { onChangeFindUserSubmit,foundUserEmail,foundUserFullName,foundUserRole } = useAdminContext();
    const { userRole } = useAuthContext();
    const [changeRole, setChangeRole] = useState(false);
 
    const { values, changeHandler, onSubmit } = useForm(
        {
          email: "",
          currentUserRole: userRole,
        },
        onChangeFindUserSubmit
      );

  return (
    <>
      <MyNavBar />
      {changeRole && <ChangeUser setChangeRole={setChangeRole} currentUserRole={foundUserRole}/>}
      <div className={style.container}>
        <h2 className={style.header}>Change User Role</h2>
        {!foundUserEmail &&
        <form onSubmit={onSubmit} className={style.innerContainer}>
        <label className={style.lyrics} htmlFor="email">
              Find user by email:
            </label>
            <input
              value={values.email}
              onChange={changeHandler}
              type="email"
              name="email"
              className={style.inp + " form-control"}
              id="email"
              placeholder="Enter email"
              required
            />
            <ButtonSubmit />
        </form>
         }
         {foundUserEmail &&
         <>
            <div className={style.found}> 
            <p>User Email: <strong className={style.innerPar}>{foundUserEmail}</strong></p>
            <p>User First and Last Name:<strong className={style.innerPar}>{foundUserFullName}</strong></p>
            <p>User Role: <strong className={style.innerPar}>{foundUserRole}</strong></p>
        </div>
        <button onClick={() => setChangeRole(true)} type="submit" className={style.loginBtn + " btn btn-primary"}>
          Change Role
          </button>
        </>
            }
      </div>
    </>
  );
};
