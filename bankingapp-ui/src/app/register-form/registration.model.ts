export interface RegistrationRequest {
    email: string | null;
    firstName: string | null;
    lastName: string | null;
    password: string | null;
    verificationRedirect?: string | null;
}