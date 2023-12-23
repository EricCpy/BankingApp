export interface TransactionRequest {
    description: string | null;
    amount: number | null;
    iban: string | null;
    bic?: string | null;
}