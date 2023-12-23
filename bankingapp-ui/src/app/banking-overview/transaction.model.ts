export interface Transaction {
    amount: number;
    sending: boolean;
    receiverIban: string;
    receiverBic: string;
}