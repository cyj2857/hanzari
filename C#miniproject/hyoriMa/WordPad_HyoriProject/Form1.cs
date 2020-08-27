using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.IO;
using System.Text.RegularExpressions;
using System.Windows.Forms;
using DevComponents.DotNetBar;

namespace WordPad_HyoriProject
{
    public partial class MyForm : Office2007RibbonForm
    {
        private List<string> fontList;
        private List<int> fontSizeList;

        public MyForm()
        {
            InitializeComponent();
        }

        private void MyForm_Load(object sender, EventArgs e)
        {
            MyFont font = new MyFont();
            fontList = font.getFontFamilies();
            foreach (string item in fontList)
            {
                fontComboBox.Items.Add(item);
            }

            fontSizeList = font.getFontSize();
            foreach (int item in fontSizeList)
            {
                fontSizeComboBox.Items.Add(item.ToString());
            }

            string fontName = richTextBox.SelectionFont.Name;
            float fontSize = richTextBox.SelectionFont.Size;
            setComboBoxSelectedIndex(fontName, fontSize);
        }

        private class MyFont
        {
            private List<string> fontFamilies = new List<string>();
            private List<int> fontsizeList = new List<int>();

            public List<string> getFontFamilies()
            {
                fontFamilies = new List<string>();
                foreach (FontFamily family in FontFamily.Families)
                {
                    fontFamilies.Add(family.Name);
                }
                return fontFamilies;
            }

            public List<int> getFontSize()
            {
                fontsizeList = new List<int>();
                for (int i = 0; i < 100; i++)
                {
                    fontsizeList.Add(i + 1);
                }
                return fontsizeList;
            }
        }

        private void fontNameComboBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (richTextBox.SelectionFont != null)
            {
                string selectedFontItem = fontComboBox.SelectedItem.ToString();
                Font currentFont = richTextBox.SelectionFont;

                richTextBox.SelectionFont = new Font(selectedFontItem, currentFont.Size, currentFont.Style);
            }
        }

        private void fontSizeComboBox_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (richTextBox.SelectionFont != null)
            {
                string selectedFontItem = fontSizeComboBox.SelectedItem.ToString();
                Font currentFont = richTextBox.SelectionFont;

                richTextBox.SelectionFont = new Font(currentFont.FontFamily, Int32.Parse(selectedFontItem), currentFont.Style);
            }
        }

        private void fontBold_CheckedChanged(object sender, EventArgs e)
        {
            if (richTextBox.SelectionFont != null)
            {
                Font currentFont = richTextBox.SelectionFont;
               
                if ((sender as ButtonItem).Checked == true)
                {
                    richTextBox.SelectionFont = new Font(currentFont.FontFamily, currentFont.Size, FontStyle.Bold);
                }
                else
                {
                    richTextBox.SelectionFont = new Font(currentFont.FontFamily, currentFont.Size, FontStyle.Regular);
                }

            }
        }

        private void colorPickerDropDown1_SelectedColorChanged(object sender, EventArgs e)
        {
            ColorPickerDropDown cpd = sender as ColorPickerDropDown;
            Color color = Color.FromArgb(cpd.SelectedColor.ToArgb());
            richTextBox.SelectionColor = color;
        }

        private void leftAlignButton_Clicked(object sender, EventArgs e)
        {
            richTextBox.SelectionAlignment = HorizontalAlignment.Left;
        }

        private void middleAlignButton_Clicked(object sender, EventArgs e)
        {
            richTextBox.SelectionAlignment = HorizontalAlignment.Center;
        }

        private void rightAlignButton_Clicked(object sender, EventArgs e)
        {
            richTextBox.SelectionAlignment = HorizontalAlignment.Right;
        }

        private void openFileButton_Click(object sender, EventArgs e)
        {
            OpenFileDialog openFileDialog = new OpenFileDialog();
            openFileDialog.Filter = "모든 워드패드 문서(*.rtf; *.docx; *.odt; *.txt)|*.rtf; *.docx; *.odt; *.txt";

            if (openFileDialog.ShowDialog() == DialogResult.OK)
            {
                StreamReader reader = new StreamReader(openFileDialog.FileName);
                Font currentFont = richTextBox.SelectionFont;

                richTextBox.Font = new Font(currentFont.Name, currentFont.Size, currentFont.Style);
                richTextBox.Text = reader.ReadToEnd();

                string fileName = Path.GetFileName(openFileDialog.FileName);
                this.Text = fileName;

                reader.Close();

                //showing recent files
                ButtonItem buttonItem = new ButtonItem();
                buttonItem.Text = fileName;
                buttonItem.Click += ButtonClickHandler;

                itemContainer.SubItems.Add(buttonItem,0);
            }

        }

        //open recent files
        private void ButtonClickHandler(object sender, EventArgs e)
        {
            string fileName = sender.ToString();
            FileInfo f = new FileInfo(fileName);
            string fullName = f.FullName;

            StreamReader reader = new StreamReader(fullName);
            Font currentFont = richTextBox.SelectionFont;

            richTextBox.Font = new Font(currentFont.Name, currentFont.Size, currentFont.Style);
            richTextBox.Text = reader.ReadToEnd();

            this.Text = fileName;

            reader.Close();
        }

        private void saveButton_Click(object sender, EventArgs e)
        {
            DialogResult result;
            result = MessageBox.Show("저장하겠습니까?", "Save", MessageBoxButtons.YesNo, MessageBoxIcon.Question);
            if (result == DialogResult.Yes)
            {
                SaveFileDialog saveFileDialog = new SaveFileDialog();
                saveFileDialog.Filter = "txt files (*.txt)|*.txt|All files (*.*)|*.*";
                string currentDirectory = Directory.GetCurrentDirectory();
                saveFileDialog.InitialDirectory = currentDirectory;

                if (saveFileDialog.ShowDialog() == DialogResult.OK)
                {
                    File.WriteAllText(saveFileDialog.FileName, richTextBox.Text);
                }
            }
        }

        private void printButton_Click(object sender, EventArgs e)
        {
            PrintDialog printDialog = new PrintDialog();
            printDialog.ShowDialog();
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            this.Close();
        }

        public void setComboBoxSelectedIndex(string fontName, float fontSize)
        {
            int fontNameIndex = fontComboBox.Items.IndexOf(fontName);
            fontComboBox.SelectedIndex = fontNameIndex;

            int fontSizeIndex = fontSizeComboBox.Items.IndexOf(fontSize.ToString());
            fontSizeComboBox.SelectedIndex = fontSizeIndex;
        }

       
    }
}
