export const createFileName = (
    additionalFolders: string = '',
    mode: 'development' | 'production' = 'development',
    extension: 'js' | 'css' = 'js',
): string => {
    if (mode === 'development') {
        return `${additionalFolders}bundle.${extension}`
    } else {
        return `${additionalFolders}[name].[contenthash].bundle.${extension}`
    }
}
