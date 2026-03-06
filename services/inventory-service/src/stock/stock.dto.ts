import { IsNumber, IsString, IsOptional, Min } from 'class-validator';

export class UpdateStockDto {
    @IsNumber()
    @Min(0)
    quantity: number;

    @IsString()
    @IsOptional()
    location?: string;
}

export class AdjustStockDto {
    @IsNumber()
    amount: number; // Valeur positive ou négative
}
