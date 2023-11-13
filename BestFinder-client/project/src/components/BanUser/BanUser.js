import { useTranslation } from 'react-i18next'
import { MyNavBar } from '../Header/MyNavBar'
import style from './BanUser.module.css'
import { useForm } from '../useForm/useForm'
import { useAuthContext } from '../AuthContext/AuthContext'
import { ButtonSubmit } from '../Login/ButtonSubmit'
import { useAdminContext } from '../AdminContext/AdminContext'
import { ErrorMessage } from '../ErrorMessage/ErrorMessage'
import { useValidatorContext } from '../ValidatorContext/ValidatorContext'
import { useEffect } from 'react'

export const BanUser = () => {
    const { t } = useTranslation();
    const { changeRoleValidateEmail } = useValidatorContext();
    const { serverErrorsAdmin, errorMessageAdmin, isChanged, setIsChanged , onClickBanUser} = useAdminContext();
    const {
      resetPage,
      onChangeFindUserSubmit,
      foundUserEmail,
      foundUserFullName,
      foundUserRole,
    } = useAdminContext();
    const { userRole } = useAuthContext();
  
    useEffect(() => {
      return () => {
        resetPage();
        setIsChanged(false)
      };
    }, []);
  
    const { values, changeHandler, onSubmit, formErrors } = useForm(
      {
        email: "",
        currentUserRole: userRole,
      },
      onChangeFindUserSubmit
    );
  
    const onEmailSearch = (e) => {
      setIsChanged(false)
      onSubmit(e, changeRoleValidateEmail(values));
    }
  
    const onButtonClick = async (e) => {
      e.preventDefault();
      await onClickBanUser({userEmail:foundUserEmail});
    }
  
    return (
      <>
        <MyNavBar />
        <div className={style.container}>
          <h2 className={style.header}>{t("ban.title")}</h2>
          {isChanged && <div className={style.success}>{t("successffully.success")}!</div>}
          { isChanged ? '' : errorMessageAdmin && <ErrorMessage message={errorMessageAdmin} />}
          {!foundUserEmail && (
            <form onSubmit={onEmailSearch} className={style.innerContainer}>
              <label className={style.lyrics} htmlFor="email">
              {t("changeUserRole.title2")}
              </label>
              <input
                value={values.email}
                onChange={changeHandler}
                type="email"
                name="email"
                className={style.inp + " form-control"}
                id="email"
                min={4}
                autoComplete="true"
                placeholder="Enter email"
                required
              />
              {formErrors.email ? <ErrorMessage message={formErrors.email}/> : 
              serverErrorsAdmin.email ? <ErrorMessage message={serverErrorsAdmin.email}/>:
              ''}
              <ButtonSubmit />
            </form>
          )}
          {foundUserEmail && (
            <>
              <div className={style.found}>
                <p>
                {t("changeRoleUserFields.email")}:
                  <strong className={style.innerPar}>{foundUserEmail}</strong>
                </p>
                <p>
                {t("changeRoleUserFields.name")}:
                  <strong className={style.innerPar}>{foundUserFullName}</strong>
                </p>
                <p>
                {t("changeRoleUserFields.role")}:
                  <strong className={style.innerPar}>{foundUserRole}</strong>
                </p>
              </div>
              <button
                onClick={onButtonClick}
                type="submit"
                className={style.loginBtn + " btn btn-primary"}
              >
                {t("ban.banuser")}
              </button>
            </>
          )}
        </div>
      </>
    );
}