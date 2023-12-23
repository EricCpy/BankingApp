export interface BankingAccount {
    IBAN: string;
    money: number;
    active: boolean;
    currency: string;
    accountType: string;
}